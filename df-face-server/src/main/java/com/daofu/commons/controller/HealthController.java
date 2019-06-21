package com.daofu.commons.controller;

import com.daofu.faceserver.controller.FaceController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author lichuang
 * @description
 * @date 2019-03-12 09:23
 */
@RestController
public class HealthController {

    @Resource
    private FaceController faceController;

    /**
     * @description 刷脸日志上报
     * @author lc
     * @date 2019-02-11 17:16
     * @return com.alibaba.fastjson.JSONObject
     */
    @PostMapping(value = "/healthcheck")
    public String healthCheck(){
        String feature = "AAD6RAAAdEPOvra9GOhcvMCOir2g5DM8PWJyPbnKuT3D80W9PKwevEvVtT1DTns8ZV2cvZFZSr12poe9ZAGAu46a4jw3xxe9SYTjPIBlt7xQGR29wbR3PTgYyj1L4b89/o9VPKpojb0S1HO9AwInvfhb0TzMUYg9Gn20PATlab2QiGK9hPEovRzBDj0Uzx492xUevf6c8LxZJ3Y9FoYfvFwEy70NG429W0+wvFFT6L1LOFK9y7ajPSGvp73+UXM7ensVPk3DgT3mwmc9rNBjPe4OCj3N05A98FadvIV9hb1FAym9r7kJvdj/cb01mTm8T0HRPZ5eFT2zBUI9V/13va8nAz0lciq98gN3PQFf2b1aPDI+jb7BPVwhJL21/rg84j2MvVEGS730x6s8vdylunUQh7vqHpK8kNlnPWzb8bwKeiS9kZiLvYZKWzpKUVi91niTPdWj5LxkKBO9u4FYPfjx6r10mly9glaHvbP6D7wZxWA9pVYrPd6uMbz9k7s6eLfkvL4zED1+cns8qIUwPcSRxDyj0RS91/otvdI3Mz3mvx4+exscvPfto7yGviW8MgEXvtYHKLy8qn+92d6mvDgdAbzVI1K9L+0rPLtbsr0WtIe8iCgBvdI9Pz6gfWS9r7uCPZwrLbx0hxI8v8iLvXWWob0gLPO9CmyIOxI3dL3PPey8aG9KPUkrD72l5jg8XP2bPHaIFT5r67c9XVOavVeb+L2jweO9EXq6vdu7ubseTGG8nnOHvbPHL7wmSEw9DGVjveqFF72f0cq9YgIlvUxtxTy5eSW+3megPFMzyD26F6E9SdqsvTEUtLwRccg8Pv5rPQGUhTyoCss9Ahm8PDxR8LvV2cM9NSWDO3nZrDw7mxY9cHqXPTXMrr01Qmo8r8uePK3JWrxMNKy9oZTRvKAFs70xXY+88X0LPdcMmDy1fSo9nAmlvX8Vgj2Nj0o8WiVcvdsAwT3RAte9m6xAPBIRIbzIqe+9ymuRPYTztrz3PFy9Y3sVPEcajr1V0Tc8aOpdvXDL/jsmWsa9pjKTvQIEnTsH79g9o3m0PPFy4bvD72K9nNasO1izhzx/Bd483NDgvMzqprzu0oC8gh2/vaPKjzzUehI+bzsYvUK2hz1h2ni9voYsvNedfL06F388iro0PRAKaj3E2a49EoSUvbiwDD20cYA9y6SePcfCyjyYK3S9VQ4/vf49XzwP4MC9c22GvNhB5LzLYQq9OyifPEWX5DxN5QW9Mb/0PQaeCLvBwZw8NFe8PbNzPTw+pFe9r2dMPe1hpz1PbcM9ZO1LPWSKBb2m70E9c8KqPQfxRjxNwbk93VGpPVgHCT3Wju26/XWqu5opTL10SFK+q3iCPB7ujz3wY9u9";
        faceController.compareFacesByFeatures(feature, feature);
        return faceController.compareFacesByFeatures(feature, feature).getScore() == 1f
                ? "success" : "fail";
    }
}
